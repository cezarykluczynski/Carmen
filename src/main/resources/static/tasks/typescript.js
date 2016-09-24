module.exports = function(grunt) {
	grunt.config.merge({
		tslint: {
			options: {
				configuration: "tslint.json"
			},
			files: {
				src: [
					"typescript/**/*.ts"
				]
			}
		},
		ts: {
			options: {
				experimentalDecorators: true,
				target: "es5",
				module: "system",
				moduleResolution: "node",
				sourceMap: true,
				emitDecoratorMetadata: true,
				removeComments: false,
				noImplicitAny: false,
				fast: 'never'
			},
			'default': {
				files: [
					{
						src: ['typescript/**/*.ts'],
						dest: '../../resources/static/dist/javascript'
					}
				]
			}
		}
	});

	grunt.loadNpmTasks("grunt-tslint");
	grunt.loadNpmTasks("grunt-ts");
};
