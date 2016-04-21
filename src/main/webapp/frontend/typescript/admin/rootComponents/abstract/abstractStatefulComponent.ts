export abstract class AbstractStatefulComponent {

	public loading: boolean;

	public constructor() {
	}

	protected setLoading(loading: boolean) {
		this.loading = loading;
	}

	public isLoading(): boolean {
		return this.loading;
	}

}
