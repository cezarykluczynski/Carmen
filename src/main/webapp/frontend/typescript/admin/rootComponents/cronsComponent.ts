import {Component} from 'angular2/core';
import {UsersImportComponent} from './crons/usersImportComponent';

@Component({
	directives: [UsersImportComponent],
	selector: 'crons-component',
	template: `
		<div class="row">
			<crons-users-import></crons-users-import>
		</div>
	`
})
export class CronsComponent {
}
