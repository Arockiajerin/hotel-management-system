import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { SnackbarService } from './snackbar.service'; 
import * as jwt_decode from 'jwt-decode';
import { GlobalConstants } from '../shared/global-constants';

@Injectable({
  providedIn: 'root'
})
export class RouterGuardService {

  constructor(
    public auth: AuthService,
    public router: Router,
    private snackbarService: SnackbarService
  ) { }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRoleArray: string[] = route.data['expectedRole'] || [];
    const token: string | null = localStorage.getItem('token');
    
    // Check if token exists
    if (!token) {
      localStorage.clear();
      this.router.navigate(['/']);
      return false;
    }

    let tokenPayload: any;
    try {
      tokenPayload = jwt_decode.jwtDecode(token);
    } catch (err) {
      localStorage.clear();
      this.router.navigate(['/']);
      return false;
    }

    // Check if token has role
    if (!tokenPayload || !tokenPayload.role) {
      localStorage.clear();
      this.router.navigate(['/']);
      return false;
    }

    // Check if user has expected role
    const hasExpectedRole = expectedRoleArray.includes(tokenPayload.role);

    if (this.auth.isAuthenticated() && hasExpectedRole) {
      return true;
    }

    this.snackbarService.openSnackBar(GlobalConstants.unauthroized, GlobalConstants.error);
    this.router.navigate(['/hotel/dashboard']);
    return false;
  }          
}