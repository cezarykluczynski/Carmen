import {it, describe, expect, beforeEach, injectAsync, inject, beforeEachProviders, TestComponentBuilder,
	ComponentFixture, setBaseTestProviders} from 'angular2/testing';
import {MockBackend} from 'angular2/http/testing';
import {MockConnection} from 'angular2/src/http/backends/mock_backend';
import {Router, APP_BASE_HREF, ROUTER_PRIMARY_COMPONENT, Location} from 'angular2/router';
import {HTTP_PROVIDERS, XHRBackend} from 'angular2/http';
import {provide} from 'angular2/core';
import {Response} from 'angular2/http';
import {SpyLocation} from 'angular2/src/mock/location_mock';
import {RootRouter} from 'angular2/src/router/router';
import {Http, ConnectionBackend} from 'angular2/http';
import {HttpClient} from './httpClient';
import {ResponseOptions} from 'angular2/http';

beforeEachProviders(() => {
	return [
		HTTP_PROVIDERS,
		provide(Router, {useClass: RootRouter}),
		provide(XHRBackend, {useClass: MockBackend}),
		provide(HttpClient, {useClass: HttpClient}),
		provide(ConnectionBackend, {useClass: MockBackend}),
		provide(Http, {useClass: Http})
	];
});

window.__carmenConfig = {
	appBaseUrl: '/'
};

describe('Service: HttpClient', () => {
	let httpClient: HttpClient;

	beforeEach(inject([HttpClient, ConnectionBackend], (_httpClient, connectionBackend) => {
		httpClient = _httpClient;

		connectionBackend.connections.subscribe((connection: MockConnection) => {
			if (connection.request.url === '/api/user/get') {
				connection.mockRespond(new Response(
					new ResponseOptions({
						body: {
							'highestGitHubUserId': null,
							'enabled': false,
							'running': false
						}
					})
				));
			} else {
				connection.mockError();
			}
		});
	}));

	it('should be defined', () => {
		expect(httpClient).not.toBe(undefined);
	});

	it('should perform GET', done => {
		httpClient.get('api/user/get').then((response) => {
			expect(true).toBe(true);
			done();
		}).catch((error) => {
			expect(false).toBe(true);
			done();
		});
	});
});
