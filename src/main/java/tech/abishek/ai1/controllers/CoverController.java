package tech.abishek.ai1.controllers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.abishek.ai1.model.Resume;
import tech.abishek.ai1.repository.ResumeRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/cover-letters")
public class CoverController {

    @Autowired
    private OpenAiChatClient aiChatClient;

    @Autowired
    private ResumeRepository resumeRepository;

    @PostMapping
    public String getCover() {

        return "Cover";
    }

    @RequestMapping(
            path = "/upload",
            method = RequestMethod.POST,
            consumes = MULTIPART_FORM_DATA_VALUE)
    public String uploadResume(
            @RequestPart("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            return String.valueOf(ResponseEntity.badRequest().body("Please upload a PDF file"));
        }

        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            Resume resume = new Resume();
            resume.setText(text);
            String id = resumeRepository.save(resume).getId().toString();
            return "resume ID: " + id + " uploaded successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return String.valueOf(ResponseEntity.badRequest().body("Failed to upload the file"));
        }


    }

    @PostMapping(path = "/generate", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getCoverLetter(@RequestParam String id, @RequestParam(name = "Job Description") String jobDescription,
                                                 @RequestParam(name = "prompt") String custPrompt) {
        String resume = resumeRepository.findById(Long.parseLong(id)).get().getText();
        String prompt = "Write a cover letter in one page just the content. The number of lines of space between paragraph should be 1. Start from the line Dear hiring manager.  In about 300 words. For this resume :\n " + resume;
        prompt = prompt + "and the job description here: \n" + jobDescription + "and the custom settings: \n" + custPrompt;

        String coverLetterText = aiChatClient.call(prompt);
        // Create a new DOCX document
        try (XWPFDocument document = new XWPFDocument()) {
            String[] paragraphs = coverLetterText.split("\n"); // Split text into paragraphs

            for (String paragraphText : paragraphs) {
                if (paragraphText.isEmpty()) {
                    continue;
                }
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(paragraphText);
                run.addBreak(); // Add line break between paragraphs
            }

            // Write the document content to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.write(outputStream);

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("filename", "cover_letter.docx");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/chat")
    public String chat(@RequestParam("message") String message) {
        return aiChatClient.call(message);
    }


}
