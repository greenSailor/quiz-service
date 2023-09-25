package com.personal.quizservice.service;

import com.personal.quizservice.dao.QuizDao;
import com.personal.quizservice.dto.QuizDto;
import com.personal.quizservice.feign.QuizInterface;
import com.personal.quizservice.model.QuestionWrapper;
import com.personal.quizservice.model.Quiz;
import com.personal.quizservice.model.Response;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    QuizInterface quizInterface;

    @Autowired
    QuizDao quizDao;

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz = quizDao.findById(id).get();

        List<Integer> questionsIds = quiz.getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questionWrapper = quizInterface.getQuestionsFromId(questionsIds);
        return questionWrapper;
    }

    public ResponseEntity<Integer> calculateScore(Integer id, List<Response> responses) {
        ResponseEntity<Integer> score = quizInterface.getScore(responses);
        return score;
    }


    public ResponseEntity<String> createQuiz(String categoryName, Integer numOfQuestions, String title) {
        List<Integer> questions = quizInterface.getQuestionsForQuiz(categoryName,numOfQuestions).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("Success",HttpStatus.CREATED);
    }
}
