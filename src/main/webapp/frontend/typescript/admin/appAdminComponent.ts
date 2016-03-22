import {Component} from 'angular2/core';
import {RouteConfig, ROUTER_DIRECTIVES, Router, Location} from 'angular2/router';
import {AdminHeaderComponent} from './header/adminHeaderComponent';
import {ServersComponent} from './rootComponents/serversComponent';
import {CronsComponent} from './rootComponents/cronsComponent';

@Component({
		selector: 'app-admin',
		template: `
			<admin-header></admin-header>
			<div class="container">
				<router-outlet></router-outlet>
			</div>
		`,
		directives: [AdminHeaderComponent, ROUTER_DIRECTIVES]
})
@RouteConfig([
	{
		path:'servers',
		name: 'Servers',
		component: ServersComponent,
		useAsDefault: true
	},
	{
		path:'crons',
		name: 'Crons',
		component: CronsComponent
	}
])
export class AppAdminComponent {
}
