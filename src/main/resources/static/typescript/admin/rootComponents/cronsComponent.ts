import {Component} from '@angular/core';
import {UsersImportComponent} from './crons/usersImportComponent';
import {SchemaUpdateComponent} from './jobs/schemaUpdateComponent';
import {LanguagesListUpdateComponent} from './jobs/languagesListUpdateComponent';
import {DatabaseSwitchableJobsComponent} from './jobs/databaseSwitchableJobsComponent';

@Component({
	directives: [UsersImportComponent, SchemaUpdateComponent, LanguagesListUpdateComponent,
		DatabaseSwitchableJobsComponent],
	selector: 'crons-component',
	template: `
		<div class="row">
			<database-switchable-jobs></database-switchable-jobs>
			<crons-users-import></crons-users-import>
			<jobs-schema-update></jobs-schema-update>
			<jobs-languages-list-update></jobs-languages-list-update>
		</div>
	`
})
export class CronsComponent {
}
