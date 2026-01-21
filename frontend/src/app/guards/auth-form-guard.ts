import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';
import { AuthService } from '../../services/AuthService';
import { filter, map, take } from 'rxjs/operators';

//Questa guard tiene controllato lo stato dell'utente, impedendo soprattutto l'accesso alle pagine
//di login o registrazione da loggati quando le si apre digitando direttamente gli URL ad esse collegati nella barra di ricerca.
//Per esempio se scrivo localhost:4200/login una guard senza questa logica permetterebbe l'accesso anche se si è già loggati.
//Con questo codice invece la guard controlla lo stato dell'utente prima che Angular crei il componente.
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
