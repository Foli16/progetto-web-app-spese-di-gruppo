import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalParticipantService {

  private readonly KEY = 'myParticipantIds';

  save(participantId: string): void {
    const ids = this.load();
    ids.push(participantId);
    localStorage.setItem(this.KEY, JSON.stringify(ids));
  }

  getAll(): string[] | null {
    const ids = this.load();
    return ids ?? null;
  }

  remove(participantId: string): void {
    const ids = this.load();
    const updatedIds = ids.filter(id => id !== participantId);
    localStorage.setItem(this.KEY, JSON.stringify(updatedIds));
  }

  private load(): string[] {
    return JSON.parse(localStorage.getItem(this.KEY) || '{}');
  }

}
