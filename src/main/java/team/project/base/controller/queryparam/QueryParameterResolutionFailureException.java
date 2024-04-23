package team.project.base.controller.queryparam;

import lombok.Getter;

@Getter
public class QueryParameterResolutionFailureException extends RuntimeException {
    public QueryParameterResolutionFailureException(String message) {
        super(message);
    }
}