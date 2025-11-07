import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { CategoryService } from 'src/app/services/category.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { CategoryComponent } from '../dialog/category/category.component';

@Component({
  selector: 'app-manage-category',
  templateUrl: './manage-category.component.html',
  styleUrls: ['./manage-category.component.scss']
})
export class ManageCategoryComponent implements OnInit {
  displayedColumns: string[] = ['name', 'edit'];
  dataSource: any;
  responseMessage: any;
  
  constructor(
    private categoryService: CategoryService,
    private ngxService: NgxUiLoaderService,
    private dialog: MatDialog,
    private snackbarService: SnackbarService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.ngxService.start();
    this.tableData();
  }

  tableData() {
    this.categoryService.getCategorys().subscribe({
      next: (response: any) => {
        this.ngxService.stop();
        console.log('API Response:', response); // Debug log
        
        // Check if response is valid
        if (response && Array.isArray(response)) {
          this.dataSource = new MatTableDataSource(response);
        } else {
          // If no data, show empty table
          this.dataSource = new MatTableDataSource([]);
          this.snackbarService.openSnackBar('No categories found', 'info');
        }
      },
      error: (error: any) => {
        this.ngxService.stop();
        console.error('Full Error Object:', error); // Detailed error log
        
        let errorMessage = GlobalConstants.genericError;
        
        if (error.status === 0) {
          errorMessage = 'Cannot connect to server. Please check if backend is running.';
        } else if (error.status === 404) {
          errorMessage = 'API endpoint not found. Please check the URL.';
        } else if (error.error?.message) {
          errorMessage = error.error.message;
        } else if (error.message) {
          errorMessage = error.message;
        }
        
        this.responseMessage = errorMessage;
        this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.error);
        
        // Initialize empty data source to prevent template errors
        this.dataSource = new MatTableDataSource([]);
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    if (this.dataSource) {
      this.dataSource.filter = filterValue.trim().toLowerCase();
    }
  }

  handleAddaction() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      action: 'Add',
    };
    dialogConfig.width = "850px";
    
    const dialogRef = this.dialog.open(CategoryComponent, dialogConfig);
    
    this.router.events.subscribe(() => {
      dialogRef.close();
    });

    const sub = dialogRef.componentInstance.onAddCategory.subscribe((response: any) => {
      this.tableData();
    });
  }

  handleEditAction(element: any) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      action: 'Edit',
      data: element
    };
    dialogConfig.width = "850px";
    
    const dialogRef = this.dialog.open(CategoryComponent, dialogConfig);
    
    this.router.events.subscribe(() => {
      dialogRef.close();
    });

    const sub = dialogRef.componentInstance.onEditCategory.subscribe((response: any) => {
      this.tableData();
    });
  }
}