import {Component} from '@angular/core';
import {UsersImportComponent} from './crons/usersImportComponent';
import {SchemaUpdateComponent} from './jobs/schemaUpdateComponent';
import {LanguagesListUpdateComponent} from './jobs/languagesListUpdateComponent';

@Component({
	directives: [UsersImportComponent, SchemaUpdateComponent, LanguagesListUpdateComponent],
	selector: 'crons-component',
	template: `
		<div class="row">
			<crons-users-import></crons-users-import>
			<jobs-schema-update></jobs-schema-update>
			<jobs-languages-list-update></jobs-languages-list-update>
		</div>
	`
})
export class CronsComponent {
}
