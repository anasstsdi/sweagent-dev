import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { User, UserInput, UserService } from '../services/user.service';

/**
 * Lists the users from the API, edits one inline (PUT), adds one (POST) and deletes one (DELETE).
 * Every mutation re-fetches the list so the table always shows what the database holds.
 */
@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <p *ngIf="error" class="error">{{ error }}</p>

    <table class="user-table">
      <thead>
        <tr><th>ID</th><th>Name</th><th>Email</th><th></th></tr>
      </thead>
      <tbody>
        <tr *ngFor="let u of users">
          <ng-container *ngIf="editingId !== u.id; else editRow">
            <td>{{ u.id }}</td>
            <td>{{ u.name }}</td>
            <td>{{ u.email }}</td>
            <td>
              <button (click)="startEdit(u)">Edit</button>
              <button (click)="remove(u)">Delete</button>
            </td>
          </ng-container>
          <ng-template #editRow>
            <td>{{ u.id }}</td>
            <td><input [(ngModel)]="draft.name" /></td>
            <td><input [(ngModel)]="draft.email" /></td>
            <td>
              <button (click)="save(u.id)">Save</button>
              <button (click)="cancelEdit()">Cancel</button>
            </td>
          </ng-template>
        </tr>
      </tbody>
    </table>

    <p class="muted" *ngIf="!users.length && !error">No users yet.</p>

    <h2>Add a user</h2>
    <form (ngSubmit)="add()">
      <input [(ngModel)]="newUser.name" name="new-name" placeholder="Name" required />
      <input [(ngModel)]="newUser.email" name="new-email" placeholder="Email" type="email" required />
      <button type="submit">Add</button>
    </form>
  `,
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  error = '';

  editingId: number | null = null;
  draft: UserInput = { name: '', email: '' };
  newUser: UserInput = { name: '', email: '' };

  constructor(private readonly userService: UserService) {}

  ngOnInit(): void {
    this.reload();
  }

  reload(): void {
    this.error = '';
    this.userService.list().subscribe({
      next: (users) => (this.users = users),
      error: () => (this.error = 'Failed to load users - is the API running on :8090?'),
    });
  }

  startEdit(u: User): void {
    this.editingId = u.id;
    this.draft = { name: u.name, email: u.email };
  }

  cancelEdit(): void {
    this.editingId = null;
  }

  save(id: number): void {
    this.userService.update(id, this.draft).subscribe({
      next: () => {
        this.editingId = null;
        this.reload();
      },
      error: () => (this.error = 'Update failed (check the email format).'),
    });
  }

  add(): void {
    if (!this.newUser.name || !this.newUser.email) {
      return;
    }
    this.userService.create(this.newUser).subscribe({
      next: () => {
        this.newUser = { name: '', email: '' };
        this.reload();
      },
      error: () => (this.error = 'Create failed (check the email format).'),
    });
  }

  remove(u: User): void {
    this.userService.remove(u.id).subscribe({
      next: () => this.reload(),
      error: () => (this.error = 'Delete failed.'),
    });
  }
}
