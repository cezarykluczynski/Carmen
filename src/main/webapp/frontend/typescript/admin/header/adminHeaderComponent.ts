import {Component, ViewChild} from 'angular2/core';
import {Router, Route, ROUTER_DIRECTIVES, RouteConfig, Location} from 'angular2/router';
import {AdminHeaderElementsProvider} from './adminHeaderElementsProvider';
import {ServersComponent} from '../rootComponents/serversComponent';
import {CronsComponent} from '../rootComponents/cronsComponent';

@Component({
	selector: 'admin-header',
	template: `
	<nav class="navbar navbar-light navbar-fixed-top bg-faded">
		<ul class="nav navbar-nav container">
			<li class="nav-item" *ngFor="#element of adminHeaderElements" [class.active]="isActive(element.name)">
				<a class="nav-link" [routerLink]="[element.name]" [innerHtml]="[element.name]">{{element.name}}</a>
			</li>
		</ul>
	</nav>
	`,
	directives: [ROUTER_DIRECTIVES],
	providers: [AdminHeaderElementsProvider]
})
@RouteConfig([
	new Route({
		path:'servers',
		name: 'Servers',
		component: ServersComponent,
		useAsDefault: true
	}),
	new Route({
		path:'crons',
		name: 'Crons',
		component: CronsComponent
	})
])
export class AdminHeaderComponent {

	adminHeaderElements: Array<any>;

	constructor(private router: Router, private adminHeaderElementsProvider: AdminHeaderElementsProvider) {
		this.router = router;
		this.adminHeaderElements = adminHeaderElementsProvider.getElements();
	}

	isActive(routeName: string): boolean {
		return this.router.isRouteActive(this.router.generate([routeName]));
	}

}
