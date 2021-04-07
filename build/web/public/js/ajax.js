export async function $ajax(params){
    
    let {url,metodo,data,header={},cs,cf} = params;
    
    let opciones = {
        method: metodo,
        body: data,
        mode: 'cors',
        cache: 'no-cache',
        headers: header
    };
    
    try
    {
       let response = await fetch(url,opciones); 
       let json = await response.json();
       
       if(!response.ok){ throw {status:response.status,text:response.statusText}; }
       cs(json);
    }
    catch(error)
    {
       console.error(error);
       cf(error);
    }    
    
}