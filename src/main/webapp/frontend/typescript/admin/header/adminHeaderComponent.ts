import {Component} from 'angular2/core';
import {Router, Route, ROUTER_DIRECTIVES, RouteConfig} from 'angular2/router';
import {AdminHeaderElementsProvider} from './adminHeaderElementsProvider';

@Component({
	directives: [ROUTER_DIRECTIVES],
	providers: [AdminHeaderElementsProvider],
	selector: 'admin-header',
	template: `
	<nav class="navbar navbar-light navbar-fixed-top bg-faded">
		<ul class="nav navbar-nav container">
			<li class="nav-item" *ngFor="#element of adminHeaderElements" [class.active]="isActive(element.name)">
				<a class="nav-link" [routerLink]="[element.name]" [innerHtml]="[element.name]">{{element.name}}</a>
			</li>
		</ul>
	</nav>
	`
})
export class AdminHeaderComponent {

	private adminHeaderElements: Array<any>;

	constructor(private router: Router, private adminHeaderElementsProvider: AdminHeaderElementsProvider) {
		this.router = router;
		this.adminHeaderElements = adminHeaderElementsProvider.getElements();
	}

	isActive(routeName: string): boolean {
		return this.router.isRouteActive(this.router.generate([routeName]));
	}

}