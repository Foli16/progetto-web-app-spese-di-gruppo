import { Routes } from '@angular/router';
import { GroupList } from './components/group-list/group-list';
import { GroupForm } from './components/group-form/group-form';
import { RegistrationForm } from './components/registration-form/registration-form';
import { LoginForm } from './components/login-form/login-form';
import { authFormGuard } from './guards/auth-form-guard';
import { GroupDetail } from './components/group-detail/group-detail';
import { ExpenseForm } from './components/expense-form/expense-form';

export const routes: Routes = [
    {path:"",component:GroupList},
    {path:"group-creation",component:GroupForm},
    {path:"registration",component:RegistrationForm,canActivate:[authFormGuard]},
    {path:"login",component:LoginForm,canActivate:[authFormGuard]},
    {path:"group-detail/:id/:partId",component:GroupDetail},
    {path:"group-detail/:id/:partId/new-expense",component:ExpenseForm}
];
