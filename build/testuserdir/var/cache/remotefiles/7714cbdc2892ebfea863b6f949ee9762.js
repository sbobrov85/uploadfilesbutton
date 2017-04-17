var sendsay_check_form;

window.onload = function() {
	// Slide Replase code
	var SRC='<div style="padding-top:40px;padding-bottom:30px;overflow:hidden;width:300px;margin: 0 auto;"><a style="float:left;" id="sendsay_form_prev" class="subpro_btn2" href="#">Назад</a><input style="float:right;display:none;" type="submit" value="Сохранить" name="bt_save" class="subpro_btn" id="sendsay_form_submit"><a style="float:right;" class="subpro_btn2" id="sendsay_form_next" href="#">Далее</a></div>';
	function wait(id,f,cnt){setTimeout(function(){wait_ready(id, f, cnt);},1);};
	function wait_ready(id,f,cnt){if(ge(id)!=null){f();return false;}else{cnt = cnt || 0;if(cnt < 1000){setTimeout(function(){wait(id,f,cnt+1)},0);}}}
	// Util
	var ge = function(s){ return document.getElementById(s); };
	var show = function(i,st){ s = st || 'block'; (typeof i === 'string'?ge(i):i).style.display=s; }
	var trim = function(s){ return s.replace(/^\s+|\s+$/g, ''); }
	var isU = function(o){return (typeof o==='undefined');}
	var isNum = function(a){return (!isNaN(parseFloat(a)) && isFinite(a))};
	var attr = function(o,s){return o.getAttribute(s);};
	var check_select_val = function(el) {var op=el.getElementsByTagName('option'),i=0;while(!isU(op[i])) {if(op[i].selected){ return true; }i+=1;}return false;};
	/* BEGIN Sliding */
	var Flist = document.getElementsByClassName('sendsayFieldItem'),
		pi = 0; // Page index (index visible field)
	// console.log('Flist, formSlide = '+formSlide);
	// console.dir(Flist);

	if(!isU(window.formSlide) && formSlide) {
		show(Flist[0]);
		if(Flist.length<=1) {
			show('sendsay_form_slidebox','none');
			// show('sendsayFormSubmitBox');
			}else{
			console.log('outerHTML');
			ge('sendsayFormSubmitBox').outerHTML = SRC;
			}
		}
		
	wait('sendsay_form_prev', function() {
		ge('sendsay_form_prev').onclick = function(e) {
			if(pi>0) { 
				show(Flist[pi],'none');
				pi--;
				show(Flist[pi],'block');
				if(pi==0) {
					show(this,'none');
					}else{
						show('sendsay_form_next');
						show('sendsay_form_submit','none');
						}
				}
			};
			
		ge('sendsay_form_next').onclick = function(e) {
			// console.log('NEXT: pi = '+pi);
			if(sendsay_check_form(Flist[pi])) {
				if(pi<(Flist.length-1)) {
					show(Flist[pi],'none');
					pi+=1;
					show(Flist[pi]);
					// console.log(pi+'=='+(Flist.length-1));
					if(pi==(Flist.length-1)) {
						show(this,'none');
						show('sendsay_form_submit');
						}else{
							show('sendsay_form_prev');
						}
					}
				}
			};
		},1);
	/* END Sliding */
	
	sendsay_check_form = function (f) { // fe = index - show only first error
		// Display Error
		var de = function(el,s){
			// console.log('Element: '+el+(typeof el==='object'?' Name: '+el.name:''));
			// console.dir(el);
			errText += "\n - "+ge('sp_label_'+((typeof el==='string')?el:el.name)).getAttribute('data-title');
			/*
			var qid=(typeof el==='string')?el:el.name;
			document.getElementById(qid+'_error').style.display=(s?'block':'none');
			*/
			haveError=true;
			};
		// Do ...
		var Elist = f.getElementsByClassName('pro_mustbe');

		// console.dir(Elist);
		var rl = { 
			yd: /^\d{4}\-\d{2}\-\d{2}$/,
			yh: /^\d{4}\-\d{2}\-\d{2} \d{2}$/,
			ym: /^\d{4}\-\d{2}\-\d{2} \d{2}\:\d{2}$/,
			ys: /^\d{4}\-\d{2}\-\d{2} \d{2}\:\d{2}\:\d{2}$/
			},nd = ['y','m','d','h','i','s'],
			mr = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,6})+$/;

		var g = {}, dates = {},dkeys=[],skeys=[], haveError=false;
		var errText = PS_ErrPref;
		
		// console.log('hsize = '+Elist.length);
		var i = 0;
		while(!isU(Elist[i])) {
			if(isU(Elist[i].tagName))break;
			var el = Elist[i],
				type = attr(el,'data-type'),
				format = attr(el,'data-format'),
				group = attr(el,'data-group'),
				part = attr(el,'data-part'),
				val=trim(el.value);
			// console.log('Tag = '+Elist[i].tagName+' group = '+group,Elist[i]);
			// console.log('['+i+'] Type = '+type+' val = '+val+(isU(part)?'':' part = '+part));
			switch(type) {
				case 'email': if(!val || !mr.test(val)){ de(el); /* de(type,1);  return alert('Введите корректное значение E-Mail');*/} break;
				case 'int': if(!val || !isNum(val)) { de(el,1); /* return alert('Введите число'); */ } break;
				case 'free': if(!val){ de(el,1); /* return alert('Введите значение'); */ } break;
				case 'dt': if(el.tagName=='input') {
								if(!val.test(rl[format])) {  de(el,1); /* return alert('Неверный формат даты'); */ }
								}else{
									if(isU(dates[group])) { dates[group]={ 'key': i }; dkeys.push(group); }
									dates[group][part] = el.value;
									}
							break;
				// case 'dt': if(!val.test(rl[format])) {  return alert('Неверный формат даты'); } break;
				}
			if(type=='1m'||type=='nm') {
				if(el.tagName=='INPUT') {
					if(isU(g[group])) { skeys.push(group); g[group]=true; }
					if(el.checked) {
							g[group]=false;
							}
						}else{
						if(!check_select_val(el)) { de(el); /* return alert('Выберите значение'); */ }
						}
				}
			i+=1;
			}
		// console.log('Groups');
		// console.dir(g);
		
		if(skeys.length) {
			// console.log('skeys = '+skeys.length);
			// console.dir(skeys);
			var k;
			for(k in skeys) {
				var c = g[skeys[k]];
				if(c) {	de(skeys[k],1); /* return alert('Выберите значение ('+skeys[k]+')'); */ }
				}
			}

		if(dkeys.length) {
			var k, p, err,s1,s2,f; // key, part key, err have, format symbol1, format symbol2, format
			// console.log('dates = ');
			// console.dir(dates);
			for(k in dkeys) {
				var d = dates[dkeys[k]], el = Elist[d['key']];
				// console.log('k = '+k,el);
				err=false;
				f = el.getAttribute('data-format');
				s1=f.valueOf()[0];
				s2=f.valueOf()[1];
				for(p in nd) {
					// console.log('Check = '+nd[p]+' = '+d[nd[p]]);
					if(isU(d[nd[p]]) || !isNum(d[nd[p]])) {
						err = true;
						}
					if(s2==nd[p]) { break; }
					}
				if(err) { de(skeys[k],1); /* return alert('Неверный формат даты'); */ }
				}
			}
		// console.log('errText = '+errText);
		// if((type=='n1' || type=='nm') && g[i]) { return alert('Не выбран ни один вариант!'); }
		if(haveError) { alert(errText); return false; }
		return !haveError;
		};
};
