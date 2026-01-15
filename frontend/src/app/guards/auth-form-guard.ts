import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';
import { AuthService } from '../../services/AuthService';
import { filter, map, take } from 'rxjs/operators';

export const authFormGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const serv = inject(AuthService);

  // if(!serv.authChecked)
  // {
  //   return false;
  // }

  // if(serv.loggedUser)
  // {
  //   router.navigate(["/"]);
  //   alert("User already logged in");
  //   return false;
  // }

  return true;

  // return serv.loggedUser?.pipe(
  //   filter(user => user !== undefined), // aspetta check auth
  //   take(1),
  //   map(user => {
  //     if (user) {
  //       router.navigate(['/']);
  //       return false;
  //     }
  //     return true;
  //   })
  // );
};
