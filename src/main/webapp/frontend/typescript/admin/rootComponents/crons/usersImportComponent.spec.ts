/// <reference path="../../../../typings/jquery/jquery.d.ts" />

import {it, describe, expect, beforeEach, inject, injectAsync, beforeEachProviders, TestComponentBuilder,
	ComponentFixture} from 'angular2/testing';
import {MockBackend} from 'angular2/http/testing';
import {Router} from 'angular2/router';
import {HTTP_PROVIDERS, XHRBackend, Http, ConnectionBackend} from 'angular2/http';
import {provide} from 'angular2/core';
import {RootRouter} from 'angular2/src/router/router';
import {HttpClient} from '../../util/httpClient';
import {HttpClientTestHelper} from '../../util/httpClientTestHelper.spec';
import {UsersImportComponent} from './usersImportComponent';
import {UsersImportApi} from './usersImportApi';

describe('Component: UsersImportComponent', () => {
	let usersImportComponent: UsersImportComponent;
	let connectionBackend: MockBackend;
	let element: any;
	let fixture: ComponentFixture;

	beforeEachProviders(() => {
		return [
			HTTP_PROVIDERS,
			provide(Router, { useClass: RootRouter }),
			provide(XHRBackend, { useClass: MockBackend }),
			provide(HttpClient, { useClass: HttpClient }),
			provide(UsersImportComponent, { useClass: UsersImportComponent }),
			provide(UsersImportApi, { useClass: UsersImportApi }),
			provide(ConnectionBackend, { useClass: MockBackend }),
			provide(Http, { useClass: Http })
		];
	});

	let createResponse = (asyncResolve: boolean, _connectionBackend) => {
		HttpClientTestHelper.createResponse({
			asyncResolve: asyncResolve,
			connectionBackend: _connectionBackend,
			unsubscribeAfter: true,
			url: '/api/admin/github/cron/users_import'
		}, {
			'highestGitHubUserId': null,
			'enabled': false,
			'running': false
		});
	};

	beforeEach(inject([ConnectionBackend], (_connectionBackend) => {
		connectionBackend = _connectionBackend;
		createResponse(false, connectionBackend);
	}));

	beforeEach(injectAsync([TestComponentBuilder], tcb => {
		return tcb
			.createAsync(UsersImportComponent)
			.then((_fixture) => {
				fixture = _fixture;
				usersImportComponent = fixture.componentInstance;
				element = fixture.nativeElement;
				fixture.detectChanges();
			});
	}));

	it('should be defined', () => {
		expect(usersImportComponent).not.toBe(undefined);
	});

	it('sets loading flag', (done) => {
		expect(usersImportComponent.loading).toBe(false);
		createResponse(true, connectionBackend);
		usersImportComponent.refreshStatus();
		expect(usersImportComponent.loading).toBe(true);

		setTimeout(() => {
			expect(usersImportComponent.loading).toBe(false);
			done();
		});
	});

	it('allows reload', (done) => {
		let $button = jQuery(element).find('button:contains(\'Refresh\')');
		expect(usersImportComponent.loading).toBe(false);
		expect($button.length).toBe(1);

		createResponse(true, connectionBackend);
		$button[0].click();
		fixture.detectChanges();
		expect(usersImportComponent.loading).toBe(true);

		setTimeout(() => {
			expect(usersImportComponent.loading).toBe(false);
			done();
		});
	});
});
