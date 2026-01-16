import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';
import { AuthService } from '../../services/AuthService';
import { filter, map, take } from 'rxjs/operators';

export const authFormGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const serv = inject(AuthService);

  return serv.user$.pipe( //concatena operatori
    filter(user => user !== undefined), //scarta il valore undefined, ossia quello che ha user all'avvio 
    take(1), //prende solo il primo valore passato
    map(user => { //trasforma il valore passato in un boolean per la guard
      if (user) {
        router.navigate(['/']);
        return false;
      }
      return true;
    })
  );
};
