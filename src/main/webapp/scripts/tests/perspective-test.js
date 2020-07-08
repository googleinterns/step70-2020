QUnit.test('perspective - toxicityToPercent (normal use)', function(assert) {
  const toxicity = 0.51;
  const result = toxicityToPercent(toxicity);
  const expected = '51.00%';
  assert.equal(result, expected, 'Expecting 51.00%');
});
