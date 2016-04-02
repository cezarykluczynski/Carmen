import {ResponseOptions} from 'angular2/http';
import {MockConnection} from 'angular2/src/http/backends/mock_backend';
import {Response} from 'angular2/http';
import {MockBackend} from 'angular2/http/testing';

export class HttpClientTestHelper {

	public static createResponse(options: {
		connectionBackend: MockBackend,
		url: string,
		asyncResolve?: boolean,
		unsubscribeAfter?: boolean
	}, body: any): any {
		window.__carmenConfig = {
			appBaseUrl: '/'
		};

		let subscription = options.connectionBackend.connections.subscribe((connection: MockConnection) => {
			if (connection.request.url === options.url) {
				this.resolve(options.asyncResolve, () => connection.mockRespond(new Response(
					new ResponseOptions({
						body: body
					})
				)));
			} else {
				this.resolve(options.asyncResolve, () => { connection.mockError(); });
			}

			if (options.unsubscribeAfter !== false) {
				subscription.unsubscribe();
			}
		});

		return subscription;
	}

	private static resolve(async: boolean, response: Function): void {
		async === true ? setTimeout(response) : response();
	}

}
