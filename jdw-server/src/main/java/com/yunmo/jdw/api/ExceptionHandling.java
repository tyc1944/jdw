package com.yunmo.jdw.api;


import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

import java.net.UnknownHostException;
import java.sql.SQLNonTransientConnectionException;

@ControllerAdvice
public class ExceptionHandling extends com.yunmo.boot.web.ExceptionHandling {

    @ExceptionHandler(InvalidBearerTokenException.class)
    public ResponseEntity<Problem> handleInvalidBearerTokenException(
            final InvalidBearerTokenException exception,
            final NativeWebRequest request) {
    return ResponseEntity.status(Status.UNAUTHORIZED.getStatusCode())
        .body(
            Problem.builder()
                .withStatus(ExtStatus.UNAUTHORIZED_INVALID_TOKEN)
                .withTitle(ExtStatus.UNAUTHORIZED_INVALID_TOKEN.getReasonPhrase())
                .withDetail(ExtStatus.UNAUTHORIZED_INVALID_TOKEN.getReasonPhrase())
                .build());
    }

    @ExceptionHandler
    public ResponseEntity<Problem> SQLNonTransientConnectionException(
            final SQLNonTransientConnectionException sqlNonTransientConnectionException) {
        return ResponseEntity.status(525).body(Problem.builder()
                .withDetail("数据库连接失败")
                .withStatus(ExtStatus.SQL_Non_Transient_Connection_Exception)
                .withTitle(ExtStatus.SQL_Non_Transient_Connection_Exception.getReasonPhrase())
                .withDetail(ExtStatus.SQL_Non_Transient_Connection_Exception.getReasonPhrase())
                .build());
    }

    @ExceptionHandler
    public ResponseEntity<Problem> UnknownHostException(
            final UnknownHostException unknownHostException) {
        return ResponseEntity.status(526).body(Problem.builder()
                .withDetail("企查查调用失败")
                .withStatus(ExtStatus.Un_known_Host_Exception)
                .withTitle(ExtStatus.Un_known_Host_Exception.getReasonPhrase())
                .withDetail(ExtStatus.Un_known_Host_Exception.getReasonPhrase())
                .build());
    }
    public  enum ExtStatus implements StatusType {

        /**
         * 401 Unauthorized, see <a href=
         * "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.2">HTTP/1.1
         * documentation</a>.
         */
        UNAUTHORIZED_INVALID_TOKEN(Status.UNAUTHORIZED.getStatusCode(), "无效的token"),
        UNAUTHORIZED_DISABLED_ACCOUNT_TOKEN(Status.UNAUTHORIZED.getStatusCode(), "账户已禁用"),
        UNAUTHORIZED_LOGIN_ACCOUNT_PASSWORD_CHANGE_TOKEN(Status.UNAUTHORIZED.getStatusCode(), "登录账户密码变更"),
        UNAUTHORIZED_LOGIN_ACCOUNT_PERMISSION_CONF_CHANGE_TOKEN(Status.UNAUTHORIZED.getStatusCode(), "登录账户权限变更"),
        UNAUTHORIZED_LOGIN_ACCOUNT_DELETED(Status.UNAUTHORIZED.getStatusCode(), "账户已删除"),
        SQL_Non_Transient_Connection_Exception(525, "数据库连接失败"),
        Un_known_Host_Exception(526,"企查查调用失败"),
        ;


        private final int code;
        private final String reason;

        ExtStatus(final int statusCode, final String reasonPhrase) {
            this.code = statusCode;
            this.reason = reasonPhrase;
        }
        /**
         * Get the associated status code.
         *
         * @return the status code.
         */
        @Override
        public int getStatusCode() {
            return code;
        }

        /**
         * Get the reason phrase.
         *
         * @return the reason phrase.
         */
        @Override
        public String getReasonPhrase() {
            return reason;
        }

        /**
         * Get the Status String representation.
         *
         * @return the status code and reason.
         */
        @Override
        public String toString() {
            return getStatusCode() + " " + getReasonPhrase();
        }

    }


}
