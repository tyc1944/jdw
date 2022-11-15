package com.yunmo.jdw.infrastruction.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.web.advice.ProblemHandling;


@ControllerAdvice
public class ExceptionHandler implements ProblemHandling {


    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<Problem> handleProblem(
            final RuntimeException problem,
            final NativeWebRequest request) {
        return create(problem, request);
    }
}
