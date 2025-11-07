import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { MatDialogRef } from '@angular/material/dialog';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from '../services/snackbar.service';
import { GlobalConstants } from '../shared/global-constants';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
   hide = true;
   loginForm: any = FormGroup;
   responseMessage: any;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private userService: UserService,
    public dialog: MatDialogRef<LoginComponent>,
    private ngxService: NgxUiLoaderService,
    private snackbarService: SnackbarService
  ) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: [null, [Validators.required, Validators.pattern(GlobalConstants.emailRegex)]],
      password: [null, [Validators.required]],
    });
  }

  handleSubmit() {
    this.ngxService.start();
    const formData = this.loginForm.value;
    const data = {
      email: formData.email,
      password: formData.password,
    };

    this.userService.login(data).subscribe(
      (response: any) => {
        this.ngxService.stop();
        
        // PERMANENT SOLUTION: Extract token properly
        const token = this.extractTokenFromResponse(response);
        
        if (token && this.isValidJWT(token)) {
          localStorage.setItem('token', token);
          this.dialog.close();
          this.router.navigate(['/hotel/dashboard']);
        } else {
          this.responseMessage = 'Invalid token received from server';
          this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
        }
      },
      (error: any) => {
        this.ngxService.stop();
        if (error.error?.message) {
          this.responseMessage = error.error?.message;
        } else {
          this.responseMessage = GlobalConstants.genericError;
        }
        this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
      }
    );
  }

  // PERMANENT SOLUTION: Extract token from any response format
  private extractTokenFromResponse(response: any): string {
    // Check different possible response formats
    if (typeof response === 'string') {
      return response; // Direct token string
    } else if (response.token) {
      return response.token; // { token: 'jwt-token' }
    } else if (response.data?.token) {
      return response.data.token; // { data: { token: 'jwt-token' } }
    } else if (response.jwt) {
      return response.jwt; // { jwt: 'jwt-token' }
    } else if (response.accessToken) {
      return response.accessToken; // { accessToken: 'jwt-token' }
    } else if (response.access_token) {
      return response.access_token; // { access_token: 'jwt-token' }
    }
    
    console.error('Token not found in response. Response structure:', response);
    return '';
  }

  // PERMANENT SOLUTION: Validate JWT token format
  private isValidJWT(token: string): boolean {
    if (!token || typeof token !== 'string') {
      return false;
    }
    
    // JWT must have exactly 2 dots (header.payload.signature)
    const parts = token.split('.');
    if (parts.length !== 3) {
      console.error('Invalid JWT format: Token must have 3 parts separated by dots');
      return false;
    }
    
    // Each part should be base64 encoded and non-empty
    if (!parts[0] || !parts[1] || !parts[2]) {
      console.error('Invalid JWT format: All parts must be non-empty');
      return false;
    }
    
    console.log('✅ Valid JWT token received and stored');
    return true;
  }
}