QUnit.test('perspective - toxicityToPercentString (normal use)', function(assert) {
  const toxicity = 0.51;
  const result = toxicityToPercentString(toxicity);
  const expected = '51.00%';
  assert.equal(result, expected, 'Expecting 51.00%');
});
