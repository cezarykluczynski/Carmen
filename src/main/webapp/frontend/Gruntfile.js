module.exports = function(grunt) {
	grunt.task.loadTasks('tasks');

	grunt.task.registerTask('build', function () {
		grunt.task.run('sass');
		grunt.task.run('vendor-js');
		grunt.task.run('vendor-css');
		grunt.task.run('tslint');
		grunt.task.run('ts');
	});

	grunt.registerTask('default', ['build', 'watch']);
};
