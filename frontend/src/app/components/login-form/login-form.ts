import { Component } from '@angular/core';
import { AuthService } from '../../../services/AuthService';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login-form',
  imports: [FormsModule],
  templateUrl: './login-form.html',
  styleUrl: './login-form.css',
})
export class LoginForm {
  constructor(public serv:AuthService)
  {}

  cred = {email:"", password:""};

  login()
  {
    this.serv.login(this.cred.email, this.cred.password);
  }
 }
