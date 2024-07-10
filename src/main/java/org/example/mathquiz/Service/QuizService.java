package org.example.mathquiz.Service;

import lombok.RequiredArgsConstructor;
import org.example.mathquiz.Entities.Quiz;

import org.example.mathquiz.Entities.QuizOption;
import org.example.mathquiz.Repositories.IQuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class QuizService {
    private final IQuizRepository levelRepository;
    public List<Quiz> getAllQuizs() {
        return levelRepository.findAll();
    }
    public Optional<Quiz> getQuizById(String id) {
        return levelRepository.findById(id);
    }
    public Quiz addQuiz(Quiz level) {
        return levelRepository.save(level);
    }
    public List<Quiz> addQuiz(List<Quiz> quizList) {
        List<Quiz> savedQuizzes = new ArrayList<>();
        for( Quiz quiz: quizList){
            savedQuizzes.add(levelRepository.save(quiz));
        }
        return savedQuizzes;
    }
    public Quiz updateQuiz(Quiz level) {
        return levelRepository.save(level);
    }
    public void deleteQuizById(String id) {
        levelRepository.deleteById(id);
    }

    public List<Quiz> readFileLatex(MultipartFile file) throws IOException {
        List<Quiz> questions = new ArrayList<>();
        if (file != null && file.getSize() > 0) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                StringBuilder fileContentBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContentBuilder.append(line).append("\n");
                }
                String fileContent = fileContentBuilder.toString();
                fileContent = fileContent.replace("%[]", "");

                String exPattern = "\\\\begin\\{ex}([\\s\\S]*?)\\\\end\\{ex}";
                Pattern pattern = Pattern.compile(exPattern, Pattern.MULTILINE);
                Matcher matcher = pattern.matcher(fileContent);

                while (matcher.find()) {
                    Quiz question = new Quiz();
                    String session = matcher.group(1).trim() + "\\end{loigiai}";
                    int indexAnswer = session.indexOf("\\choice");
                    String questionName = session.substring(0, indexAnswer);
                    String tikzPattern = "\\\\begin\\{tikzpicture\\}(.*?)\\\\end\\{tikzpicture\\}";

                    Matcher tikzMatcher = Pattern.compile(tikzPattern, Pattern.DOTALL).matcher(questionName);
                    if (tikzMatcher.find()) {
                        String tikzContent = "\\begin{tikzpicture}" + tikzMatcher.group(1) + "\\end{tikzpicture}";
                        questionName = questionName.replace(tikzContent, "")
                                .replace("\\centerline{", "")
                                .replace("}\\", "")
                                .replace("\\begin{center}", "")
                                .replace("\\end{center}", "");
                        question.setImage("https://i.upmath.me/svg/" + UriEncoder.encodeURIComponent(tikzContent.trim()));
                    }
                    question.setStatement(questionName.replace("\\immini[thm]{", "").trim());

                    String answerPattern = "\\\\choice([\\s\\S]*?)\\\\loigiai\\{";
                    Pattern answerPatternCompile = Pattern.compile(answerPattern);
                    Matcher answerMatcher = answerPatternCompile.matcher(session);
                    List<QuizOption> answers = new ArrayList<>();
                    while (answerMatcher.find()) {
                        String fullAnswer = answerMatcher.group(1).trim();
//                        System.out.println(fullAnswer);
//                        System.out.println("AAAAA");
                        String innerPattern = "\\{([^{}]+|\\{([^{}]+|\\{([^{}]+|\\{[^{}]*\\})*\\})*\\})*\\}";
                        Matcher innerMatcher = Pattern.compile(innerPattern).matcher(fullAnswer);
//                        while (innerMatcher.find()) {
//                            System.out.println("Found match: " + innerMatcher.group());
//                        }

                        while (innerMatcher.find()) {
                            QuizOption answer = new QuizOption();
                            answer.setOption(innerMatcher.group().replaceAll("^\\{|\\}$", ""));
                            Matcher innerTikzMatcher = Pattern.compile(tikzPattern, Pattern.DOTALL).matcher(innerMatcher.group());
                            if (innerTikzMatcher.find()) {
                                String isImage = "\\begin{tikzpicture}" + innerTikzMatcher.group(1) + "\\end{tikzpicture}";
                                answer.setQuizOptionImage("https://i.upmath.me/svg/" + UriEncoder.encodeURIComponent(isImage.trim()));
                                answer.setOption(answer.getOption().replace(isImage, ""));
                            }

                            if (answer.getOption().contains("\\True")) {
                                answer.setIsCorrect(true);
                                answer.setOption(answer.getOption().replace("\\True", "").trim());
                            }
                            answers.add(answer);
                        }
                    }
                    question.setQuizOptions(answers);

                    String solutionPattern = "\\\\loigiai\\{([\\s\\S]*?)\\}\\\\end\\{loigiai\\}";
                    Matcher solutionMatcher = Pattern.compile(solutionPattern).matcher(session);
                    while (solutionMatcher.find()) {
                        String solutionContent = solutionMatcher.group(1).trim();
                        Matcher solutionTikzMatcher = Pattern.compile(tikzPattern, Pattern.DOTALL).matcher(solutionContent);
                        if (solutionTikzMatcher.find()) {
                            String isImageSolution = "\\begin{tikzpicture}" + solutionTikzMatcher.group(1) + "\\end{tikzpicture}";
                            question.setImageSolution("https://i.upmath.me/svg/" + UriEncoder.encodeURIComponent(isImageSolution.trim()));
                            solutionContent = solutionContent.replace(isImageSolution, "")
                                    .replace("\\centerline{", "")
                                    .replace("\\begin{center}", "")
                                    .replace("\\end{center}", "")
                            ;
                        }
                        question.setSolution(solutionContent);
                    }

                    questions.add(question);
                }
            }
        }
        return questions;
    }
    private static class UriEncoder {
        public static String encodeURIComponent(String s) {
            String result;
            try {
                result = java.net.URLEncoder.encode(s, "UTF-8")
                        .replaceAll("\\+", "%20")
                        .replaceAll("\\%21", "!")
                        .replaceAll("\\%27", "'")
                        .replaceAll("\\%28", "(")
                        .replaceAll("\\%29", ")")
                        .replaceAll("\\%26", "&")
                        .replaceAll("\\%3D", "=")
                        .replaceAll("\\%7E", "~");
            } catch (java.io.UnsupportedEncodingException e) {
                result = s;
            }
            return result;
        }
    }

}
