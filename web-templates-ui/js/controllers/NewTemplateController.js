app.controller('NewTemplateController', ['$scope', function($scope) {
  $scope.templateName = '';
  $scope.templateContent = '';
  
  $scope.save = save;
  $scope.onEvaluate = onEvaluate;
  $scope.remove = remove;
  

  $scope.ckEditorOptions = {};
  $scope.ckEditorReady = function () {};
}]);


function save(templateName, templateContent) {
}

function onEvaluate() {
}

function remove(templateName) {
  const confirmed = confirm("¿Está seguro de remover la plantilla?");
  if (!confirmed) {
    return false;
  }
  
}
