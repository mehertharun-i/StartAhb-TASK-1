import { inject } from '@angular/core';
import {
  HttpEvent, HttpHandler, HttpRequest, HttpErrorResponse, HttpHandlerFn, HttpInterceptorFn
} from '@angular/common/http';
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { catchError, filter, switchMap, take } from 'rxjs/operators';

let refreshInProgress = false;
const refreshSubject = new BehaviorSubject<string | null>(null);

export const AuthInterceptor: HttpInterceptorFn = (
  req: HttpRequest<any>,
  next: HttpHandlerFn
): Observable<HttpEvent<any>> => {
  const auth = inject(AuthService);
  const token = auth.getAccessToken();

  let authReq = req;
  if (token) {
    authReq = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }

  return next(authReq).pipe(
    catchError((err: any) => {
      if (err instanceof HttpErrorResponse && err.status === 401) {
        if (!refreshInProgress) {
          refreshInProgress = true;
          refreshSubject.next(null);

          return auth.refreshToken().pipe(
            switchMap((res: any) => {
              refreshInProgress = false;
              refreshSubject.next(res.accessToken);
              const newReq = req.clone({
                setHeaders: { Authorization: `Bearer ${res.accessToken}` }
              });
              return next(newReq);
            }),
            catchError((error: any) => {
              refreshInProgress = false;
              auth.logout();
              return throwError(() => error);
            })
          );
        } else {
          // Wait for refresh to finish
          return refreshSubject.pipe(
            filter((t) => !!t),
            take(1),
            switchMap((newToken) => {
              const newReq = req.clone({
                setHeaders: { Authorization: `Bearer ${newToken}` }
              });
              return next(newReq);
            })
          );
        }
      }
      return throwError(() => err);
    })
  );
};
