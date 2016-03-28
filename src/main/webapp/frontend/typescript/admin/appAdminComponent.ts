import {Component} from 'angular2/core';
import {AdminHeaderComponent} from './header/adminHeaderComponent';
import {ROUTER_DIRECTIVES} from 'angular2/router';

@Component({
		directives: [AdminHeaderComponent, ROUTER_DIRECTIVES],
		selector: 'app-admin',
		template: `
			<admin-header></admin-header>
			<div class="container">
				<router-outlet></router-outlet>
			</div>
		`
})
export class AppAdminComponent {
}
