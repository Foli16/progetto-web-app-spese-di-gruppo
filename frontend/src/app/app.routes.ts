import { Routes } from '@angular/router';
import { Home } from './components/Home/Home';
import { GroupForm } from './components/group-form/group-form';
import { RegistrationForm } from './components/registration-form/registration-form';
import { LoginForm } from './components/login-form/login-form';
import { authFormGuard } from './guards/auth-form-guard';

export const routes: Routes = [
    {path:"",component:Home},
    {path:"form",component:GroupForm},
    {path:"registration",component:RegistrationForm,canActivate:[authFormGuard]},
    {path:"login",component:LoginForm,canActivate:[authFormGuard]}
];
