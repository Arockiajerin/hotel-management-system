import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private router: Router) { }

  public isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    return !!token; // Simply return boolean, let guard handle navigation
  }

  public logout(): void {
    localStorage.clear();
    this.router.navigate(['/']);
  }
}