const mockPerspectiveResponse = new Response(
  '{"attributeScores":{"TOXICITY":{"summaryScore":{"value":"0"}}}}',
  { "status" : 200 }
);

QUnit.test('getToxicity (no comment input)', function(assert) {
  const done = assert.async();
  getDomValue = sinon.fake.returns('');
  updateDom = sinon.fake();
  getToxicity().then(() => {
    assert.equal(updateDom.firstArg, NO_COMMENT_ERROR.message, 'no comment error message text');
    done();
  });
});

QUnit.test('getToxicity (comment length exceeded)', function(assert) {
  const done = assert.async();
  getDomValue = sinon.fake.returns('A'.repeat(3001));
  updateDom = sinon.fake();
  getToxicity().then(() => {
    assert.equal(updateDom.firstArg, COMMENT_LENGTH_EXCEEDED_ERROR.message, 'comment length exceeded message text');
    done();
  });
});

QUnit.test('getToxicityFromPerspectiveApi', function(assert) {
  const done = assert.async();
  const comment = 'normal comment';
  const TOXICITY = 0;
  sinon.stub(window, 'fetch');
  window.fetch.returns(Promise.resolve(mockPerspectiveResponse));
  getToxicityFromPerspectiveApi().then((response) => {
    assert.equal(response, TOXICITY, 'score is 0');
    window.fetch.restore();
    done();
  });
});

QUnit.test('toxicityToPercentString', function(assert) {
  const toxicity = 0.51;
  const result = toxicityToPercentString(toxicity);
  const expected = '51.0% chance of being toxic';
  assert.equal(result, expected, 'Expecting 51.00%');
});
