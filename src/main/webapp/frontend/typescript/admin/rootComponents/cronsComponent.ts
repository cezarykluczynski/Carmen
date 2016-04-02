import {Component} from 'angular2/core';
import {UsersImportComponent} from './crons/usersImportComponent';

@Component({
	directives: [UsersImportComponent],
	selector: 'crons-component',
	template: `
		<crons-users-import></crons-users-import>
	`
})
export class CronsComponent {
}
