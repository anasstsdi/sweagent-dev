import { Component } from '@angular/core';

import { UserListComponent } from './users/user-list.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [UserListComponent],
  template: `
    <h1>Users</h1>
    <p class="muted">Served by Spring Boot on :8090 (H2 in-memory), proxied through <code>/api</code>.</p>
    <app-user-list />
  `,
})
export class AppComponent {}
