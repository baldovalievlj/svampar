import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {LoginForm} from "../../domain/forms/login-form";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginRequest} from "../../domain/requests/login-request";

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  @Input() loading = false;
  @Input() errorMessage: string | null = null;

  @Output() submit= new EventEmitter<LoginRequest>()
  hidePassword: boolean = true
  form: FormGroup<LoginForm>

  constructor() {
    this.form = new FormGroup({
      username: new FormControl('', {nonNullable: true, validators: Validators.required}),
      password: new FormControl('', {nonNullable: true, validators: Validators.required})
    })
  };

  onSubmit(){
    this.submit.emit(this.form.getRawValue())
  }
}
