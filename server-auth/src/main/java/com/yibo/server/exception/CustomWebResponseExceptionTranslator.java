package com.yibo.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * @author: huangyibo
 * @Date: 2019/11/15 17:18
 * @Description: 自定义 CustomWebResponseExceptionTranslator 实现认证服务器的异常信息处理
 */

@Component
@Slf4j
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    /**
     * 目前实现的OAuth2Exception，定义返回的异常在OAuth2Exception类中
     * @param exception
     * @return
     * @throws Exception
     */
    @Override
    public ResponseEntity translate(Exception exception) throws Exception {
        //OAuth2Exception异常
        if (exception instanceof OAuth2Exception) {
            OAuth2Exception oAuth2Exception = (OAuth2Exception) exception;
            if(oAuth2Exception instanceof InvalidGrantException){
                //
                return ResponseEntity
                        .status(oAuth2Exception.getHttpErrorCode())
                        .body(new CustomOAuth2Exception("用户名或密码错误"));
            }
            return ResponseEntity
                    .status(oAuth2Exception.getHttpErrorCode())
                    .body(new CustomOAuth2Exception(oAuth2Exception.getMessage()));
        }
        //username不正确手动抛出的BadCredentialsException会进入到这里来
        else if(exception instanceof AuthenticationException){
            AuthenticationException authenticationException = (AuthenticationException) exception;
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new CustomOAuth2Exception(authenticationException.getMessage()));
        }
        //其余的异常会直接返回
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CustomOAuth2Exception(exception.getMessage()));


        //下面实现的方法和上面一样，但是太复杂，这里不采用，只做参考

        /*Throwable[] causeChain = this.throwableAnalyzer.determineCauseChain(e);
        Exception ase = (OAuth2Exception)this.throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);
        //异常链中有OAuth2Exception异常
        if (ase != null) {
            return this.handleOAuth2Exception((OAuth2Exception)ase);
        }
        //身份验证相关异常
        ase = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class,
                causeChain);
        if (ase != null) {
            return handleOAuth2Exception(new UnauthorizedException(e.getMessage(), e));
        }

        //异常链中包含拒绝访问异常
        ase = (AccessDeniedException) throwableAnalyzer
                .getFirstThrowableOfType(AccessDeniedException.class, causeChain);
        if (ase instanceof AccessDeniedException) {
            return handleOAuth2Exception(new ForbiddenException(ase.getMessage(), ase));
        }

        //异常链中包含Http方法请求异常
        ase = (HttpRequestMethodNotSupportedException) throwableAnalyzer
                .getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class, causeChain);
        if (ase instanceof HttpRequestMethodNotSupportedException) {
            return handleOAuth2Exception(new MethodNotAllowed(ase.getMessage(), ase));
        }

        // 不包含上述异常则服务器内部错误
        return handleOAuth2Exception(new ServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e));*/

    }

//    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    /*private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) throws IOException {
        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        if (status == HttpStatus.UNAUTHORIZED.value() || e instanceof InsufficientScopeException) {
            headers.set("WWW-Authenticate", String.format("%s %s", "Bearer", e.getSummary()));
        }
        CustomOAuth2Exception exception = new CustomOAuth2Exception(e.getMessage(),e);
        ResponseEntity<OAuth2Exception> response = new ResponseEntity(exception, headers, HttpStatus.valueOf(status));
        return response;
    }

    *//**
     * 异常链中包含Http方法请求异常
     *//*
    private static class MethodNotAllowed extends OAuth2Exception {
        public MethodNotAllowed(String msg, Throwable t) {
            super(msg, t);
        }
        @Override
        public String getOAuth2ErrorCode() {
            return "method_not_allowed";
        }
        @Override
        public int getHttpErrorCode() {
            return 405;
        }
    }

    *//**
     * 身份验证相关异常
     *//*
    private static class UnauthorizedException extends OAuth2Exception {
        public UnauthorizedException(String msg, Throwable t) {
            super(msg, t);
        }
        @Override
        public String getOAuth2ErrorCode() {
            return "unauthorized";
        }
        @Override
        public int getHttpErrorCode() {
            return 401;
        }
    }

    *//**
     * 服务器内部错误
     *//*
    private static class ServerErrorException extends OAuth2Exception {
        public ServerErrorException(String msg, Throwable t) {
            super(msg, t);
        }
        @Override
        public String getOAuth2ErrorCode() {
            return "server_error";
        }
        @Override
        public int getHttpErrorCode() {
            return 500;
        }
    }

    *//**
     * 异常链中包含拒绝访问异常
     *//*
    private static class ForbiddenException extends OAuth2Exception {
        public ForbiddenException(String msg, Throwable t) {
            super(msg, t);
        }
        @Override
        public String getOAuth2ErrorCode() {
            return "access_denied";
        }
        @Override
        public int getHttpErrorCode() {
            return 403;
        }
    }*/

}
