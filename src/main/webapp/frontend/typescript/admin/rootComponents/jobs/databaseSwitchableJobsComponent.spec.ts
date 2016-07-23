/// <reference path="../../../../typings/jquery/jquery.d.ts" />

import {it, describe, expect, beforeEach, injectAsync, inject, beforeEachProviders} from '@angular/core/testing';
import {TestComponentBuilder} from '@angular/compiler/testing/test_component_builder';
import {ComponentFixture} from '@angular/compiler/testing/test_component_builder';
import {HTTP_PROVIDERS, XHRBackend, Http, ConnectionBackend, RequestMethod} from '@angular/http';
import {Router} from '@angular/router-deprecated';
import {RootRouter} from '@angular/router-deprecated/src/router';
import {MockBackend} from '@angular/http/testing';
import {HttpClient} from '../../util/httpClient';
import {provide} from '@angular/core';
import {HttpClientTestHelper} from '../../util/httpClientTestHelper.spec';
import {DatabaseSwitchableJobsComponent} from './databaseSwitchableJobsComponent';
import {DatabaseSwitchableJobsApi} from './databaseSwitchableJobsApi';
import {DatabaseSwitchableJobDTO} from './dto/databaseSwitchableJobDTO';
import {COMPILER_PROVIDERS, XHR} from '@angular/compiler';
import {MockXHR} from '@angular/compiler/testing';

describe('Component: DatabaseSwitchableJobsComponent', () => {
	let databaseSwitchableJobsComponent: DatabaseSwitchableJobsComponent;
	let connectionBackend: MockBackend;
	let element: any;
	let fixture: ComponentFixture<DatabaseSwitchableJobsComponent>;

	let createResponse = (asyncResolve: boolean, _connectionBackend, method: RequestMethod, response?: Object,
		error?: Error) => {
		HttpClientTestHelper.createResponse({
			asyncResolve: asyncResolve,
			connectionBackend: _connectionBackend,
			unsubscribeAfter: true,
			method,
			error,
			url: '/api/admin/cron/database_switchable_job'
		}, response);
	};

	beforeEachProviders(() => {
		return [
			HTTP_PROVIDERS,
			COMPILER_PROVIDERS,
			provide(XHR, { useClass: MockXHR }),
			provide(Router, { useClass: RootRouter }),
			provide(XHRBackend, { useClass: MockBackend }),
			provide(HttpClient, { useClass: HttpClient }),
			provide(DatabaseSwitchableJobsComponent, { useClass: DatabaseSwitchableJobsComponent }),
			provide(TestComponentBuilder, { useClass: TestComponentBuilder }),
			provide(DatabaseSwitchableJobsApi, { useClass: DatabaseSwitchableJobsApi }),
			provide(ConnectionBackend, { useClass: MockBackend }),
			provide(Http, { useClass: Http })
		];
	});

	function createDTO(name: String, enabled?: Boolean): DatabaseSwitchableJobDTO {
		let dto: DatabaseSwitchableJobDTO = new DatabaseSwitchableJobDTO();
		dto.name = name;
		dto.enabled = enabled;
		return dto;
	};

	beforeEach(injectAsync([TestComponentBuilder], tcb => {
		return tcb
			.createAsync(DatabaseSwitchableJobsComponent)
			.then((_fixture) => {
				fixture = _fixture;
				databaseSwitchableJobsComponent = fixture.componentInstance;
				element = fixture.nativeElement;
				fixture.detectChanges();
			});
	}));

	beforeEach(inject([ConnectionBackend], (_connectionBackend) => {
		connectionBackend = _connectionBackend;
	}));

	it('should be defined', () => {
		expect(databaseSwitchableJobsComponent).not.toBe(undefined);
	});

	it('reloads on initializaton, then sort dtos', (done) => {
		let dtos: Array<DatabaseSwitchableJobDTO> = new Array<DatabaseSwitchableJobDTO>();
		dtos.push(createDTO('b'));
		dtos.push(createDTO('c'));
		dtos.push(createDTO('a'));

		createResponse(true, connectionBackend, RequestMethod.Get, dtos);
		databaseSwitchableJobsComponent.reload();
		fixture.detectChanges();

		setTimeout(() => {
			expect(databaseSwitchableJobsComponent.databaseSwitchableJobsList.length).toBe(3);
			expect(databaseSwitchableJobsComponent.databaseSwitchableJobsList[0].name).toBe('a');
			expect(databaseSwitchableJobsComponent.databaseSwitchableJobsList[1].name).toBe('b');
			expect(databaseSwitchableJobsComponent.databaseSwitchableJobsList[2].name).toBe('c');
			done();
		});
	});

	it('updates list, then reload', (done) => {
		HttpClientTestHelper.createResponse({
			asyncResolve: true,
			connectionBackend,
			method: RequestMethod.Post,
			unsubscribeAfter: true,
			url: '/api/admin/cron/database_switchable_job/update_list'
		}, {});
		spyOn(databaseSwitchableJobsComponent, 'reload');
		databaseSwitchableJobsComponent.updateList();
		fixture.detectChanges();

		setTimeout(() => {
			expect(databaseSwitchableJobsComponent.reload).toHaveBeenCalled();
			done();
		});
	});

	describe('status', () => {
		it('when dto is enabled, it is retrieved as string', () => {
			let dto: DatabaseSwitchableJobDTO = createDTO('name', true);
			expect(databaseSwitchableJobsComponent.getStatus(dto)).toBe('enabled');
		});

		it('when dto is disabled, it is retrieved as string', () => {
			let dto: DatabaseSwitchableJobDTO = createDTO('name', false);
			expect(databaseSwitchableJobsComponent.getStatus(dto)).toBe('disabled');
		});
	});

	describe('button class', () => {
		it('when dto is enabled, it is retrieved as string', () => {
			let dto: DatabaseSwitchableJobDTO = createDTO('name', true);
			expect(databaseSwitchableJobsComponent.getButtonClass(dto)).toBe('btn-success');
		});

		it('when dto is disabled, it is retrieved as string', () => {
			let dto: DatabaseSwitchableJobDTO = createDTO('name', false);
			expect(databaseSwitchableJobsComponent.getButtonClass(dto)).toBe('btn-danger');
		});
	});

	describe('status', () => {
		it('should toggle status when it is disabled', (done) => {
			HttpClientTestHelper.createResponse({
				asyncResolve: true,
				connectionBackend,
				method: RequestMethod.Post,
				unsubscribeAfter: true,
				url: '/api/admin/cron/database_switchable_job/enable'
			}, {});
			spyOn(databaseSwitchableJobsComponent, 'reload');
			databaseSwitchableJobsComponent.toggleStatus(createDTO('dto', false));
			fixture.detectChanges();

			setTimeout(() => {
				expect(databaseSwitchableJobsComponent.reload).toHaveBeenCalled();
				done();
			});
		});

		it('should toggle status when it is disabled', (done) => {
			HttpClientTestHelper.createResponse({
				asyncResolve: true,
				connectionBackend,
				method: RequestMethod.Post,
				unsubscribeAfter: true,
				url: '/api/admin/cron/database_switchable_job/disable'
			}, {});
			spyOn(databaseSwitchableJobsComponent, 'reload');
			databaseSwitchableJobsComponent.toggleStatus(createDTO('dto', true));
			fixture.detectChanges();

			setTimeout(() => {
				expect(databaseSwitchableJobsComponent.reload).toHaveBeenCalled();
				done();
			});
		});
	});
});
