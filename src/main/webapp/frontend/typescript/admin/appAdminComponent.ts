import {Component} from '@angular/core';
import {AdminHeaderComponent} from './header/adminHeaderComponent';
import {Route, ROUTER_DIRECTIVES, RouteConfig} from '@angular/router-deprecated';
import {ServersComponent} from './rootComponents/serversComponent';
import {CronsComponent} from './rootComponents/cronsComponent';

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
@RouteConfig([
	new Route({
		component: ServersComponent,
		name: 'Servers',
		path:'servers',
		useAsDefault: true
	}),
	new Route({
		component: CronsComponent,
		name: 'Crons',
		path:'crons'
	})
])
export class AppAdminComponent {
}
