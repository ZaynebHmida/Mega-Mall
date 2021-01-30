<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Categorie;
class CategorieController extends Controller
{
	  public function __construct()
    {
        $this->middleware('auth');
    }
    public function index()
    {
        $listeCategorie=Categorie::all();
        return view('home')->with('categories',$listeCategorie);
    }
	public function create()
    {
        return view('categorie.add-categorie');
    }

    public function show($id)
    {
        //
    }

    public function store(Request $request)
    {
       $categorie= new Categorie();
        $categorie->Nom=$request->Nom;
         $categorie->save();
        return redirect('categorie');
    }
  public function edit($id)
    {  $categories= Categorie::find($id);
        return view('categorie.list-categorie')->with('categorie',$categories);
    }
    public function update(Request $request, $id)
    {
        $categorie= Categorie::find($id);
        $categorie->Nom=$request->Nom;
		$categorie->save();
        return redirect('categorie');
    }

    public function destroy($id)
    {
        $categorie= Categorie::find($id);
        $categorie->delete();
        return redirect('categorie');
    }
}
