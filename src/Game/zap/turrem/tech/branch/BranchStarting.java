
<!-- saved from url=(0130)https://raw.github.com/eekysam/Turrem/b12c3c07404c79638139375ef20561976833914b/src/Game/zap/turrem/tech/branch/BranchStarting.java -->
<html><object type="{0C55C096-0F1D-4F28-AAA2-85EF591126E7}" cotype="cs" id="cosymantecbfw" style="width: 0px; height: 0px; display: block;"></object><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><script type="text/javascript" id="waxCS">var WAX = function () { var _arrInputs; return { getElement: function (i) { return _arrInputs[i]; }, setElement: function(i){ _arrInputs=i; } } }(); function waxGetElement(i) { return WAX.getElement(i); } function coSetPageData(t, d){ if('wax'==t) { WAX.setElement(d);} }</script></head><body style=""><pre style="word-wrap: break-word; white-space: pre-wrap;">package zap.turrem.tech.branch;

import zap.turrem.tech.TechBase;

public class BranchStarting extends Branch
{
	public BranchStarting(int tech)
	{
		super(tech);
	}

	public BranchStarting(String tech)
	{
		super(tech);
	}

	public BranchStarting(TechBase tech)
	{
		super(tech);
	}

	public BranchStarting(Class&lt;? extends TechBase&gt; tech, int pass)
	{
		super(tech, pass);
	}
}
</pre></body></html>