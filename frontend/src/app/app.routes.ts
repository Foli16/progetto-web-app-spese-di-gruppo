import { Routes } from '@angular/router';
import { Home } from './components/Home/Home';
import { GroupForm } from './components/group-form/group-form';
import { RegistrationForm } from './components/registration-form/registration-form';
import { LoginForm } from './components/login-form/login-form';

export const routes: Routes = [
    {path:"",component:Home},
    {path:"form",component:GroupForm},
    {path:"registration",component:RegistrationForm},
    {path:"login",component:LoginForm}
];
