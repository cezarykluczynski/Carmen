import {Component} from '@angular/core';
import {UsersImportComponent} from './crons/usersImportComponent';
import {SchemaUpdateComponent} from './jobs/schemaUpdateComponent';

@Component({
	directives: [UsersImportComponent, SchemaUpdateComponent],
	selector: 'crons-component',
	template: `
		<div class="row">
			<crons-users-import></crons-users-import>
			<jobs-schema-update></jobs-schema-update>
		</div>
	`
})
export class CronsComponent {
}
