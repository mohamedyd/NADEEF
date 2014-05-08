/*
 * QCRI, NADEEF LICENSE
 * NADEEF is an extensible, generalized and easy-to-deploy data cleaning platform built at QCRI.
 * NADEEF means "Clean" in Arabic
 *
 * Copyright (c) 2011-2013, Qatar Foundation for Education, Science and Community Development (on
 * behalf of Qatar Computing Research Institute) having its principle place of business in Doha,
 * Qatar with the registered address P.O box 5825 Doha, Qatar (hereinafter referred to as "QCRI")
 *
 * NADEEF has patent pending nevertheless the following is granted.
 * NADEEF is released under the terms of the MIT License, (http://opensource.org/licenses/MIT).
 */

define([
    'requester',
    'state',
    'ace',
    'mvc/editor/EREditor',
    'text!mvc/template/ruleeditor.template.html'
], function(
    Requester,
    State,
    Ace,
    EREditor,
    RuleEditorTemplate
) {
    var types = {
        'FD' : EREditor,
        'UDF' : EREditor,
        'ER' : EREditor,
        'DC' : EREditor
    };

    function RuleEditorView(dom, rule) {
        this.dom = dom;
        this.rule = rule;
        this.codeEditor = null;
        this.ruleEditor = null;
        var __ = this;
        this.shownEvent = function () {
            // render the graphical editor
            // 138 is the space for header and footer, and padding of the body.
            var height = $(__.dom).height() - 138;
            __.ruleType.trigger('change');
            $(__.dom).find('.modal-body').css({ height: height + 'px'});

            // initialize the code editor
            __.codeEditor = Ace.edit("ace-editor");
            __.codeEditor.setFontSize(14);
            __.codeEditor.session.setMode("ace/mode/java");
            $(__.dom).find('#ace-editor').css({ height: (height - 100) + 'px'});
        };

        this.generateEvent = function() {
            var rule = __.getRule();
            if (!_.isNull(rule)) {
                Requester.doGenerate(rule, {
                    success: function(data) {
                        info("Code generation succeeded.");
                        __.codeEditor.setValue(data['data'], -1);
                        $('#rule-editor-tab a[href="#code-editor"]').tab("show");
                    },
                    failure: err
                });
            }
        };

        this.verifyEvent = function () {
            var rule = __.getRule();
            if (!_.isNull(rule)) {
                rule.code = __.codeEditor.getValue();
                if (!_.isNull(rule.code) || _.isEmpty(rule.code)) {
                    err("No Java code is found in the editor.");
                    return;
                }

                Requester.doGenerate(rule, {
                    success: function() { info("Verification succeeded."); },
                    failure: err
                });
            }
        };

        this.saveEvent = function () {
            var rule = __.getRule();
            if (!_.isNull(rule)) {
                Requester.createRule(rule, {
                    success: function() {
                        $(__.dom).modal('hide');
                        // TODO: change to event bubbling
                        $("#controller")[0].dispatchEvent(
                            new CustomEvent(
                                "refreshRuleEvent", {
                                    "detail" : { "info" : "You have successfully created a rule."}
                            })
                        );
                    }, failure: err
                });
            }
        };
    }

    RuleEditorView.prototype.getRule = function() {
        var type = this.ruleType.val(),
            table1 = this.table1.val(),
            table2 = this.table2.val() === table1 ? null : this.table2.val(),
            ruleName = $('#rule-name').val();

        if (_.isNull(ruleName) || _.isEmpty(ruleName)) {
            err("Rule name cannot be empty.");
            return null;
        }

        if (_.isEmpty(table1)) {
            err('No table is selected');
            return null;
        }

        var code =
            (this.ruleType.val() === 'UDF') ?
                this.codeEditor.getValue() : this.ruleEditor.val();
        if (_.isNull(code) || _.isEmpty(code)) {
            err("No content is found, you need to create something.");
            return null;
        }

        return {
            code : code,
            name : ruleName,
            type : type,
            table1: table1,
            table2: table2
        };
    };


    RuleEditorView.prototype.render = function() {
        var sources = State.get("source");
        $(this.dom).html(
            _.template(RuleEditorTemplate) ({
                name: this.rule.name,
                sources : sources,
                table1: this.rule.table1,
                table2: this.rule.table2,
                type: this.rule.type
            })
        );

        this.ruleType = $(this.dom).find("#rule-type");
        this.table1 = $(this.dom).find("#table1");
        this.table2 = $(this.dom).find("#table2");

        $(this.dom).find('#save').on('click', this.saveEvent);
        $(this.dom).find('#generate').on('click', this.generateEvent);
        $(this.dom).find('#verify').on('click', this.verifyEvent);

        var __ = this;
        var initEditor = function() {
            __.ruleEditor = new types[__.ruleType.val()].Create(
                $(__.dom).find("#advance-editor"),
                __.table1.val(),
                __.table2.val(),
                __.rule
            );

            __.ruleEditor.render();
        };

        this.ruleType.change(initEditor);
        this.table1.change(initEditor);
        this.table2.change(initEditor);

        $(this.dom).one('shown', this.shownEvent);
        $(this.dom).modal();
    };


    function info(msg) {
        $('#cleanPlanView-alert-info').alert('close');
        $('#cleanPlanView-alert').html([
            ['<div class="alert alert-success" id="cleanPlanView-alert-info">'],
            ['<button type="button" class="close" data-dismiss="alert">'],
            ['&times;</button>'],
            ['<span><h4>' + msg + '</h4></span></div>']].join(''));
        window.setTimeout(function() { $('#cleanPlanView-alert-info').alert('close'); }, 3000);
    }

    function err(msg) {
        var txt = _.isString(msg) ? msg : JSON.parse(msg.responseText)['error'];
        $('#cleanPlanView-alert').html([
            ['<div class="alert alert-error">'],
            ['<button type="button" class="close" data-dismiss="alert">'],
            ['&times;</button>'],
            ['<span><h4>' + txt + '</h4></span></div>']].join(''));
    }

    return {
        Create : RuleEditorView
    };
});