import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog'; // Fixed import
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader'; // Fixed import
import { SnackbarService } from 'src/app/services/snackbar.service';
import { UserService } from 'src/app/services/user.service';
import { GlobalConstants } from 'src/app/shared/global-constants';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  oldPassword = true;
  newPassword = true;
  confirmPassword = true;
  changePasswordForm: any = FormGroup;
  responseMessage: any;

  constructor(
    private formBuilder: FormBuilder,
    private snackbarService: SnackbarService,
    private userService: UserService,
    private ngxService: NgxUiLoaderService,
    public dialogRef: MatDialogRef<ChangePasswordComponent> // Fixed typo: dialogReg -> dialogRef
  ) { }

  ngOnInit(): void {
    this.changePasswordForm = this.formBuilder.group({
      oldPassword: [null, Validators.required],
      newPassword: [null, Validators.required],
      confirmPassword: [null, Validators.required]
    });
  }

  validateSubmit() { // Fixed typo: validateSumit -> validateSubmit
    if (this.changePasswordForm.controls['newPassword'].value !== this.changePasswordForm.controls['confirmPassword'].value) {
      // Fixed logic: should compare newPassword with confirmPassword, not oldPassword
      return true;
    } else {
      return false;
    }
  }

  handlePasswordChange() { // Fixed typo: handlepasswordChange -> handlePasswordChange
    if (this.validateSubmit()) {
      this.snackbarService.openSnackBar('New Password and Confirm Password do not match', 'error');
      return;
    }

    this.ngxService.start();
    var formData = this.changePasswordForm.value;
    var data = {
      oldPassword: formData.oldPassword,
      newPassword: formData.newPassword,
      confirmPassword: formData.confirmPassword
    }

    this.userService.changePassword(data).subscribe((response: any) => {
      this.ngxService.stop();
      this.responseMessage = response?.message;
      this.dialogRef.close(); // Fixed: dialogReg -> dialogRef
      this.snackbarService.openSnackBar(this.responseMessage, 'success');
    }, (error) => {
      console.log(error);
      this.ngxService.stop();
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericError;
      }
      this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
    });
  }
}