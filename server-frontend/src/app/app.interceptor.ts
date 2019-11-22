import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpResponse
} from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

/** Pass untouched request through to the next request handler. */
@Injectable()
export class RefreshInterceptor implements HttpInterceptor {

  constructor(private http: HttpClient) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      tap(
        () => {},
        error => {
          console.log(error);
          if (error.status === 500 && error.error.message === 'refresh fail') {
            // this.logout();
            //这里当refresh_token失效之后，不退出，而是又发请求到认证中心做认证
            //这样就可以让用户感受到app上面登录一次之后就再也不用登录的效果
            window.location.href = 'http://auth.yibo.com:9090/oauth/authorize?' +
              'client_id=serverFrontend&' +
              'redirect_uri=http://frontend.yibo.com:8080/oauth/callback&' +
              'response_type=code&' +
              'state=abc';
          }
        }));
  }

  logout() {
    this.http.post('logout', {}).subscribe(() => {
      window.location.href = 'http://auth.yibo.com:9090/logout?redirect_uri=http://frontend.yibo.com:8080';
    });
  }
}
