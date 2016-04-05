$(function () {
  
  function crateCKEditorOnElementId(elementId) {
    if (!elementId) return null;

    var wysiwygareaAvailable = isWysiwygareaAvailable(),
      isBBCodeBuiltIn = !!CKEDITOR.plugins.get('bbcode');

    var editorElement = CKEDITOR.document.getById(elementId);

    // :(((
    if (isBBCodeBuiltIn) {
      editorElement.setHtml(
        'Hello world!\n\n' +
        'I\'m an instance of [url=http://ckeditor.com]CKEditor[/url].'
      );
    }

    var ieOlderThan10 = CKEDITOR.env.ie && CKEDITOR.env.version < 9;
    if (ieOlderThan10) {
      CKEDITOR.tools.enableHtml5Elements(document);
    }

    var config = {
      height: 400,
      width: 'auto'
    };

    function isWysiwygareaAvailable() {
      // If in development mode, then the wysiwygarea must be available.
      // Split REV into two strings so builder does not replace it :D.
      if (CKEDITOR.revision == ( '%RE' + 'V%' )) {
        return true;
      }

      return !!CKEDITOR.plugins.get('wysiwygarea');
    }

    // Depending on the wysiwygare plugin availability initialize classic or inline editor.
    if (wysiwygareaAvailable) {
      return CKEDITOR.replace(elementId, config);
    }

    editorElement.setAttribute('contenteditable', 'true');
    return CKEDITOR.inline(elementId, config);
  }

  function crateJsonEditorOnElementId(elementId) {
    if (!elementId) return null;

    var container = document.getElementById(elementId);
    var options = {
      mode: 'tree',
      name: 'Contexto:',
      
    };
    var editor = new JSONEditor(container, options);

    editor.set({
      name: "Daniel",
      dog: {
        name: 'Federico',
        age: 5,
        breed: 'Pug'
      },
      items: [
        {
          name: "Shower",
          price: "USD 3.000"
        },
        {
          name: "Nail Trimming",
          price: "USD 18.000"
        }
      ]
    });
    
    return editor;
  }

  var ckEditor = crateCKEditorOnElementId('ckeditor');
  var jsonEditor = crateJsonEditorOnElementId('jsoneditor');
  
  var $eval = $('#evaluate');
  var $output = $('#output');
  
  $eval.click(function() {
    var url = "/api/eval";

    var data = {
      template: JSON.stringify(ckEditor.getData()),
      context: JSON.stringify(jsonEditor.get())
    };
    
    var success = function(data, status, request) {
      $output.html(JSON.parse(data) || '');
    };

    var error = function(data, status, request) {
      console.log("Imposible generar plantilla, error al conectar con servidor");
      console.log('\tdata', data);
      console.log('\tstatus', status);
      console.log('\trequest', request);
      alert("Imposible generar plantilla, error al conectar con servidor");
    };

    $.ajax({
      type: "POST",
      url: url,
      data: data,
      success: success,
      error: error
    });

  })

});


