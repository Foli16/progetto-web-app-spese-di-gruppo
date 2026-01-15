import { Component } from '@angular/core';
import { AuthService } from '../../../services/AuthService';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-registration-form',
  imports: [FormsModule],
  templateUrl: './registration-form.html',
  styleUrl: './registration-form.css',
})
export class RegistrationForm {
  
  constructor(private serv:AuthService)
  {}
  
  credentials = {
    email: '',
    password: '',
    confirmPassword: ''
  };
  
  registration()
  {
    if(!this.passwordCheck())
    {
      alert("le password non corrispondono");
      return;
    }
    this.serv.register(this.credentials.email, this.credentials.password);
  }

  passwordCheck()
  {
    return this.credentials.password == this.credentials.confirmPassword;
  }
 }
