import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  url = environment.apiUrl;
  
  constructor(private httpClient: HttpClient) { }

  add(data: any) {
    return this.httpClient.post(this.url + '/product/add', data, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    }).pipe(catchError(this.handleError));
  }

  update(data: any) {
    return this.httpClient.put(this.url + '/product/update', data, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    }).pipe(catchError(this.handleError));
  }

  getProducts() {
    return this.httpClient.get(this.url + '/product/get')
      .pipe(catchError(this.handleError));
  }

  updateStatus(data: any) {
    return this.httpClient.post(this.url + '/product/updateStatus', data, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    }).pipe(catchError(this.handleError));
  }

  delete(id: any) {
    return this.httpClient.delete(this.url + '/product/delete/' + id, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    }).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Unknown error occurred';
    
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    
    console.error('API Error:', errorMessage);
    return throwError(errorMessage);
  }
}