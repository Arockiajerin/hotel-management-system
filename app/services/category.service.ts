import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  url = environment.apiUrl;
  
  constructor(private httpClient: HttpClient) { }

  add(data: any) {
    return this.httpClient.post(this.url + "/category/add", data, { // Added slash
      headers: new HttpHeaders().set('Content-Type', "application/json")
    })
  }

  update(data: any) {
    return this.httpClient.post(this.url + "/category/update", data, { // Added slash
      headers: new HttpHeaders().set('Content-Type', "application/json")
    })
  }

  getCategorys(){
    return this.httpClient.get(this.url + "/category/get"); // Added slash
  }
}